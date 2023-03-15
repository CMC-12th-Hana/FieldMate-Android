import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

object ImageUtil {
    private const val MAX_WIDTH = 1280
    private const val MAX_HEIGHT = 960

    private fun optimizeBitmap(context: Context, uri: Uri): String? {
        try {
            val storage = context.cacheDir
            val fileName = String.format("%s.%s", UUID.randomUUID(), "jpg")

            val tempFile = File(storage, fileName)
            tempFile.createNewFile()

            val fos = FileOutputStream(tempFile)

            decodeBitmapFromUri(uri, context)?.apply {
                compress(Bitmap.CompressFormat.JPEG, 50, fos)
                recycle()
            } ?: throw NullPointerException()

            fos.flush()
            fos.close()

            return tempFile.absolutePath
        } catch (e: Exception) {
            Log.e("이미지 생성 실패", "${e.message}")
        }

        return null
    }

    private fun decodeBitmapFromUri(uri: Uri, context: Context): Bitmap? {
        val input = BufferedInputStream(context.contentResolver.openInputStream(uri))

        input.mark(input.available()) // 입력 스트림의 특정 위치를 기억

        var bitmap: Bitmap?

        BitmapFactory.Options().run {
            inJustDecodeBounds = true
            bitmap = BitmapFactory.decodeStream(input, null, this)

            input.reset()

            inSampleSize = calculateInSampleSize(this)
            inJustDecodeBounds = false

            bitmap = BitmapFactory.decodeStream(input, null, this)?.apply {
                rotateImageIfRequired(context, this, uri)
            }
        }

        input.close()

        return bitmap
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > MAX_HEIGHT || width > MAX_WIDTH) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= MAX_HEIGHT && halfWidth / inSampleSize >= MAX_WIDTH) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun rotateImageIfRequired(context: Context, bitmap: Bitmap, uri: Uri): Bitmap? {
        val input = context.contentResolver.openInputStream(uri) ?: return null

        val exif =
            ExifInterface(input)

        return when (exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateImage(bitmap: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun bitmapResize(
        context: Context,
        name: String,
        uriList: List<Uri>
    ): List<MultipartBody.Part>? {
        val pathHashMap = hashMapOf<Int, String?>()

        uriList.forEachIndexed { index, uri ->
            val path = optimizeBitmap(context, uri)
            pathHashMap[index] = path
        }

        val fileList = arrayListOf<MultipartBody.Part>()

        pathHashMap.forEach {
            if (it.value.isNullOrEmpty()) {
                return null
            }

            val image = File(it.value!!)
            val requestBody = image.asRequestBody("image/*".toMediaTypeOrNull())
            val part =
                MultipartBody.Part.createFormData(name, image.name, requestBody)
            fileList.add(part)
        }

        return fileList.toList()
    }
}

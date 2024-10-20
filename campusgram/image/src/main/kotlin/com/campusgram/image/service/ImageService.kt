package com.campusgram.image.service

import com.campusgram.image.controller.Image
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.flux
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class ImageService(
    private val redisTemplate: ReactiveStringRedisTemplate
) {


    suspend fun findImageById(imageId: Long): Image? {
        val image = redisTemplate.opsForValue()
            .get(imageId.toString())
            .awaitFirstOrNull() ?: return null

        return toImage(image)
    }

    private fun toImage(raw: String): Image{
        val (id, url, width, height) = raw.split(",")

        return Image(id.toLong(), url, width.toInt(), height.toInt())
    }

    fun findImagesByIds(imageIds: List<Long>): Flux<Image> {
        return flux{

            val images = redisTemplate.opsForValue()
                .multiGet(imageIds.map{it.toString()})
                .awaitSingle()

            images.filterNotNull().map { toImage(it) }.forEach { this.send(it) }
        }
    }
}
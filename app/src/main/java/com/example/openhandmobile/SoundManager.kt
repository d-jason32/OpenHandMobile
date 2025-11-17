package com.example.openhandmobile

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import kotlin.collections.set
import kotlin.let

object SoundManager {
    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<String, Int>()
    private var isInitialized = false

    fun init(context: Context) {
        if (isInitialized) return
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        soundMap["click"] = soundPool!!.load(context, R.raw.soundeffect, 1)
        soundMap["hi"] = soundPool!!.load(context, R.raw.hi_there_final, 1)
        soundMap["welcome"] = soundPool!!.load(context, R.raw.welcome_sound, 1)
        soundMap["skill"] = soundPool!!.load(context, R.raw.skill, 1)
        soundMap["practice"] = soundPool!!.load(context, R.raw.practice, 1)
        soundMap["questions"] = soundPool!!.load(context, R.raw.questions, 1)
        soundMap["nice"] = soundPool!!.load(context, R.raw.nice, 1)
        soundMap["level"] = soundPool!!.load(context, R.raw.level, 1)







        isInitialized = true
    }

    fun play(name: String) {
        soundMap[name]?.let { id ->
            soundPool?.play(id, 1f, 1f, 1, 0, 1f)
        }
    }

    fun release() {
        soundPool?.release()
        isInitialized = false
    }
}
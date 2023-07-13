package com.dem.blazeball.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dem.blazeball.model.Vector2
import com.dem.blazeball.databinding.FragmentGameBinding
import kotlin.math.atan2
import kotlin.math.sqrt

class GameFragment : Fragment() {

    init {
        System.loadLibrary("game")
    }
    private external fun isBallScored(ballPosition: Vector2, hoopPosition: Vector2, hoopRadius: Float): Boolean

    private lateinit var binding: FragmentGameBinding
    private var score: Int = 0
    private lateinit var timer: CountDownTimer

    private var ballPosition = Vector2(0f, 0f)
    private var hoopPosition = Vector2(0f, 0f)
    private var hoopRadius: Float = 0f

    private var initialBallPosition = Vector2(0f, 0f)
    private var isSwipeStarted = false

    private var ballRadius = 0
    private var ballDiameter = 0
    private var screenWidth = 0
    private var screenHeight = 0
    private val ballSpeed = 30f
    private var isScored = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(layoutInflater)

        binding.ballView.post {
            initialBallPosition.x = binding.ballView.x
            initialBallPosition.y = binding.ballView.y
        }
        binding.hoopView.post {
            hoopRadius = binding.hoopView.width.toFloat() / 2
            hoopPosition.x = binding.hoopView.x + hoopRadius
            hoopPosition.y = binding.hoopView.y + hoopRadius
        }

        ballRadius = binding.ballView.width / 2
        ballDiameter = binding.ballView.width
        screenWidth = resources.displayMetrics.widthPixels
        screenHeight = resources.displayMetrics.heightPixels

        binding.ballView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isSwipeStarted = true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = initialBallPosition.x - event.rawX
                    val dy = initialBallPosition.y - event.rawY

                    val angleRad = atan2(dy, dx).toDouble()

                    val angleDeg = Math.toDegrees(angleRad).toFloat()

                    binding.arrowView.rotation = (angleDeg + 90) % 360
                }
                MotionEvent.ACTION_UP -> {
                    if (isSwipeStarted) {
                        val ballCenterX = binding.ballView.x + binding.ballView.width / 2
                        val ballCenterY = binding.ballView.y + binding.ballView.height / 2

                        val trajectory = Vector2(ballCenterX - event.rawX, ballCenterY - event.rawY)
                        animateBall(trajectory)
                        isSwipeStarted = false
                        binding.arrowView.visibility = View.INVISIBLE
                    }
                }
            }
            true
        }


        timer = object: CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val formattedTime = String.format("%02d", millisUntilFinished / 1000)
                binding.timeValueTv.text = formattedTime
            }

            override fun onFinish() {
                val action = GameFragmentDirections.actionGameFragmentToGameOverFragment(score)
                findNavController().navigate(action)
            }
        }.start()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.ballView.post {
            checkIfScored()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            binding.ballView.x += trajectory.x
            binding.ballView.y += trajectory.y

            ballPosition = Vector2(binding.ballView.x, binding.ballView.y)
            if (!isScored) {
                checkIfScored()

                if (binding.ballView.x <= 0 || binding.ballView.x + ballDiameter > screenWidth) {
                    trajectory.x = -trajectory.x
                }

                if (binding.ballView.y <= 0) {
                    trajectory.y = -trajectory.y
                }

                if (binding.ballView.y + ballDiameter > screenHeight) {
                    resetBallPosition()
                } else {
                    handler.postDelayed(this, 16)
                }
            } else {
                resetBallPosition()
            }
        }
    }

    private var trajectory = Vector2(0f, 0f)

    private fun animateBall(trajectory: Vector2) {
        val magnitude = sqrt(trajectory.x * trajectory.x + trajectory.y * trajectory.y)

        val direction = Vector2(trajectory.x / magnitude, trajectory.y / magnitude)

        this.trajectory = Vector2(direction.x * ballSpeed, direction.y * ballSpeed)

        handler.post(runnable)
    }

    private fun resetBallPosition() {
        isScored = false
        binding.ballView.x = initialBallPosition.x
        binding.ballView.y = initialBallPosition.y
        binding.arrowView.visibility = View.VISIBLE
        binding.arrowView.rotation = 0f
    }

    private fun checkIfScored() {
        if (isBallScored(ballPosition, hoopPosition, hoopRadius)) {
            isScored = true
            score++
            val formattedScore = String.format("%02d", score)
            binding.scoreValueTv.text = formattedScore
        }
    }

}
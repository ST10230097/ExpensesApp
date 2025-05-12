package com.example.expensesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensesapp.databinding.ActivityAchievementsBinding
import com.example.expensesapp.databinding.DialogAchievementDetailBinding
import com.example.expensesapp.databinding.ItemAchievementBinding
import com.example.expensesapp.models.Achievement
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AchievementsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAchievementsBinding
    private lateinit var achievementAdapter: AchievementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAchievementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupAchievementsList()
        updateProgressSummary()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setupAchievementsList() {
        achievementAdapter = AchievementAdapter { achievement ->
            showAchievementDetail(achievement)
        }

        binding.achievementsRecyclerView.apply {
            layoutManager = GridLayoutManager(this@AchievementsActivity, 2)
            adapter = achievementAdapter
        }

        // Sample data - replace with actual data from your database
        val achievements = listOf(
            Achievement(
                id = 1,
                name = "Budget Master",
                description = "Stay under budget for 3 consecutive months",
                progress = 2,
                total = 3,
                reward = "100 points",
                isUnlocked = false,
                unlockDate = null,
                tips = listOf(
                    "Track your expenses daily",
                    "Set realistic budget goals",
                    "Review your spending patterns"
                )
            ),
            Achievement(
                id = 2,
                name = "Saving Streak",
                description = "Save money for 5 consecutive days",
                progress = 3,
                total = 5,
                reward = "50 points",
                isUnlocked = false,
                unlockDate = null,
                tips = listOf(
                    "Set aside a fixed amount daily",
                    "Use automatic savings features",
                    "Monitor your savings progress"
                )
            ),
            // Add more achievements as needed
        )

        achievementAdapter.submitList(achievements)
    }

    private fun updateProgressSummary() {
        // Sample data - replace with actual calculations
        val totalAchievements = 20
        val unlockedAchievements = 15
        val completionPercentage = (unlockedAchievements.toFloat() / totalAchievements * 100).toInt()

        binding.overallProgressIndicator.progress = completionPercentage
        binding.completionPercentageText.text = "$completionPercentage% Complete"
        binding.badgesUnlockedText.text = "$unlockedAchievements of $totalAchievements badges unlocked"
        binding.levelText.text = "Level 5 - Budget Master"
    }

    private fun showAchievementDetail(achievement: Achievement) {
        val dialogBinding = DialogAchievementDetailBinding.inflate(layoutInflater)
        
        dialogBinding.apply {
            detailBadgeIcon.setImageResource(if (achievement.isUnlocked) 
                R.drawable.ic_achievement_unlocked else R.drawable.ic_achievement_locked)
            detailAchievementName.text = achievement.name
            detailDescription.text = achievement.description
            
            if (achievement.isUnlocked) {
                detailUnlockDate.visibility = View.VISIBLE
                detailTips.visibility = View.GONE
                detailUnlockDate.text = "Unlocked on ${formatDate(achievement.unlockDate)}"
            } else {
                detailUnlockDate.visibility = View.GONE
                detailTips.visibility = View.VISIBLE
                detailTips.text = buildTipsText(achievement.tips)
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setPositiveButton("Close", null)
            .show()
    }

    private fun formatDate(date: Date?): String {
        if (date == null) return ""
        return SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(date)
    }

    private fun buildTipsText(tips: List<String>): String {
        return "Tips to unlock:\n" + tips.joinToString("\n") { "• $it" }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private class AchievementAdapter(
        private val onAchievementClick: (Achievement) -> Unit
    ) : RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder>() {

        private var achievements: List<Achievement> = emptyList()

        fun submitList(newList: List<Achievement>) {
            achievements = newList
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
            val binding = ItemAchievementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return AchievementViewHolder(binding)
        }

        override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
            holder.bind(achievements[position])
        }

        override fun getItemCount() = achievements.size

        inner class AchievementViewHolder(
            private val binding: ItemAchievementBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            init {
                binding.root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onAchievementClick(achievements[position])
                    }
                }
            }

            fun bind(achievement: Achievement) {
                binding.apply {
                    badgeIcon.setImageResource(if (achievement.isUnlocked) 
                        R.drawable.ic_achievement_unlocked else R.drawable.ic_achievement_locked)
                    lockIcon.visibility = if (achievement.isUnlocked) View.GONE else View.VISIBLE
                    achievementName.text = achievement.name
                    progressText.text = "${achievement.progress}/${achievement.total}"
                    rewardText.text = achievement.reward
                }
            }
        }
    }
} 
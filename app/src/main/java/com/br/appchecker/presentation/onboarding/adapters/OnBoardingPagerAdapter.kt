package com.br.appchecker.presentation.onboarding.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.br.appchecker.databinding.OnboardingPageItemBinding
import com.br.appchecker.presentation.onboarding.enums.OnBoardingPage

class OnBoardingPagerAdapter(
    private val onBoardingPageList: Array<OnBoardingPage> = OnBoardingPage.values()) :
    RecyclerView.Adapter<OnBoardingPagerAdapter.PagerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PagerViewHolder =
        LayoutInflater.from(parent.context).let {
            OnboardingPageItemBinding.inflate(
                it, parent, false
            ).let { binding -> PagerViewHolder(binding) }
        }


    override fun getItemCount() = onBoardingPageList.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(onBoardingPageList[position])
    }

    inner  class PagerViewHolder(private val binding: OnboardingPageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(onBoardingPage: OnBoardingPage) {
            val res = binding.root.context.resources
            binding.subTitleTv.text = res.getString(onBoardingPage.subTitleResource)
            binding.descTV.text = res.getString(onBoardingPage.descriptionResource)
            binding.img.setImageResource(onBoardingPage.logoResource)
        }

    }
}
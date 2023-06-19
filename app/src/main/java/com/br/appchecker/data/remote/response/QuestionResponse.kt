package com.br.appchecker.data.remote.response

import com.google.gson.annotations.SerializedName

data class QuestionResponse(
   @SerializedName("pergunta")
   val question: QuestionData,
   @SerializedName("opcoes")
   val answers: List<AnswersData>
)
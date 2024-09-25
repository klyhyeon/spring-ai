package com.example.demo

import com.example.demo.model.Request
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.image.ImagePrompt
import org.springframework.ai.model.Media
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.OpenAiImageModel
import org.springframework.ai.openai.OpenAiImageOptions
import org.springframework.stereotype.Service
import org.springframework.util.MimeTypeUtils
import java.net.URI

@Service
class AiService(
    private val openAiChatModel: OpenAiChatModel,
    private val openAiImageApi: OpenAiImageModel,
) {

    fun chatbot(request: Request) {
        val promptTemplateDetail = PromptTemplate(
            """
               당신은 의사야. 그래서 환자의 증상을 바탕으로 환자의 병을 예측해줘.
               환자가 자신의 증상을 말하면 너의 지식으로 최대한 잘 친절하게 알려줘.
               환자 : 배가 너무 아파요
               답변 : 배탈인 것 같습니다.
               환자 : 머리가 너무 아파요
               답변 : 두통인 것 같습니다.
               환자 : 기침을 해요.
               답변 : 감기인 것 같습니다.
               이런 형식으로 대답해줘.
               답변 : 감기인 것 같습니다. 하지만 기침이 계속되거나 심해지면 다른 호흡기 질환일 가능성도 있으니 의사의 진료를 받는 것이 좋습니다. -> 이런 식으로 대답하지 말고 내가 말한대로 간결해서 대답해줘.
               하지만 한 문장으로 약간 자세하게 대답해줘.
               환자 : ${request.text}
            """.trimIndent()
        )
        val promptTemplateDoctor = PromptTemplate(
            """
               당신은 의사야. 그래서 환자의 증상을 바탕으로 환자의 병을 예측해줘.
               환자가 자신의 증상을 말하면 너의 지식으로 최대한 잘 친절하게 알려줘.
               환자 : {message}
            """.trimIndent()
        )
        val prompt = promptTemplateDoctor.create(mapOf("message" to request.text))
        println(openAiChatModel.call(prompt).result.output.content)
    }

    fun imageAI(request: Request): String {
//        val prompt = "너는 음식 감별사야 사진을 보고 이 사진으로 할 수 있는 요리를 알려줘."
        val prompt = "사진을 보고 뭔지 판단해줘."
        val userMessage = UserMessage(prompt, listOf(Media(MimeTypeUtils.IMAGE_JPEG, URI(request.text).toURL())))
        return openAiChatModel.call(userMessage)
    }

    fun imageGenAI(request: Request): String {
        val prompt = "사용자가 글을 입력하면 너는 고흐의 화풍으로 그림을 그려줘 \n"
        return openAiImageApi.call(
            ImagePrompt(
                prompt + request.text,
                OpenAiImageOptions.builder()
                    .withQuality("hd")
                    .withN(1)
                    .withHeight(1024)
                    .withWidth(1024)
                    .build(),
            ),
        ).result.output.url
    }


}
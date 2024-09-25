package com.example.demo.controller

import com.example.demo.AiService
import com.example.demo.model.Request
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ai")
class AiController(
    private val openAiChatModel: OpenAiChatModel,
    private val aiService: AiService,
) {

//    @PostMapping
//    fun test(
//        @RequestBody request: Request,
//    ) {
//        println(openAiChatModel.call(request.text))
//    }

    @PostMapping
    fun test(
        @RequestBody request: Request,
    ) {
        aiService.chatbot(request)
    }

    @PostMapping("/image")
    fun imageAI(
        @RequestBody request: Request,
    ): String {
        return aiService.imageAI(request)
    }

    @PostMapping("/gen")
    fun imageGenAI(
        @RequestBody request: Request,
    ): String {
        return aiService.imageGenAI(request)
    }
}
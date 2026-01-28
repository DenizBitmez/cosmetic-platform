package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.Answer;
import com.cosmeticPlatform.CosmeticPlatform.model.Question;
import com.cosmeticPlatform.CosmeticPlatform.service.QAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/qa")
@CrossOrigin(origins = "http://localhost:5173")
public class QAController {

    @Autowired
    private QAService qaService;

    @PostMapping("/question")
    public ResponseEntity<Question> askQuestion(@RequestBody Map<String, Object> payload) {
        try {
            Integer userId = Integer.parseInt(payload.get("userId").toString());
            Integer productId = Integer.parseInt(payload.get("productId").toString());
            String content = (String) payload.get("content");

            // Extract optional product details for creation
            String productName = (String) payload.get("productName");
            String productBrand = (String) payload.get("productBrand");
            String productImage = (String) payload.get("productImage");
            String description = (String) payload.get("description");
            Double productPrice = payload.get("productPrice") != null
                    ? Double.parseDouble(payload.get("productPrice").toString())
                    : 0.0;

            return ResponseEntity.ok(qaService.askQuestion(userId, productId, content, productName, productBrand,
                    productImage, productPrice, description));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/answer")
    public ResponseEntity<Answer> answerQuestion(@RequestBody Map<String, Object> payload) {
        try {
            Integer userId = Integer.parseInt(payload.get("userId").toString());
            Long questionId = Long.parseLong(payload.get("questionId").toString());
            String content = (String) payload.get("content");

            return ResponseEntity.ok(qaService.answerQuestion(userId, questionId, content));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Question>> getQuestions(@PathVariable Integer productId) {
        return ResponseEntity.ok(qaService.getQuestionsByProduct(productId));
    }

    @PostMapping("/question/{id}/vote")
    public ResponseEntity<Question> voteQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(qaService.voteQuestion(id));
    }

    @PostMapping("/answer/{id}/vote")
    public ResponseEntity<Answer> voteAnswer(@PathVariable Long id) {
        return ResponseEntity.ok(qaService.voteAnswer(id));
    }
}

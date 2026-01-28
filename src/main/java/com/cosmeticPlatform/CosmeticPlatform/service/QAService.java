package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.Answer;
import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import com.cosmeticPlatform.CosmeticPlatform.model.Question;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.repository.AnswerRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.ProductRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.QuestionRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QAService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Question askQuestion(Integer userId, Integer productId, String content, String productName,
            String productBrand, String productImage, Double productPrice, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            // Create product if it doesn't exist (for external products)
            product = new Product();
            product.setId(productId);
            product.setName(productName != null ? productName : "Unknown Product");
            product.setCategory(productBrand); // Using brand as category or type if needed
            product.setImage(productImage);
            product.setPrice(productPrice != null ? productPrice : 0.0);
            product.setDescription(description);
            // Default values
            product.setStock(100);
            product = productRepository.save(product);
        }

        Question question = new Question();
        question.setUser(user);
        question.setProduct(product);
        question.setContent(content);

        return questionRepository.save(question);
    }

    public Answer answerQuestion(Integer userId, Long questionId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        Answer answer = new Answer();
        answer.setUser(user);
        answer.setQuestion(question);
        answer.setContent(content);

        // Simple expert check logic (can be expanded later with UserType)
        if (user.getUserType() != null && user.getUserType().name().equalsIgnoreCase("dermatologist")) {
            answer.setExpertAnswer(true);
        }

        return answerRepository.save(answer);
    }

    public List<Question> getQuestionsByProduct(Integer productId) {
        return questionRepository.findByProductIdOrderByCreatedDateDesc(productId);
    }

    public Question voteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        question.setUpvotes(question.getUpvotes() + 1);
        return questionRepository.save(question);
    }

    public Answer voteAnswer(Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));
        answer.setUpvotes(answer.getUpvotes() + 1);
        return answerRepository.save(answer);
    }
}

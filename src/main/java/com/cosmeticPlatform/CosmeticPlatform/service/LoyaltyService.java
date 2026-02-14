package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.LoyaltyPoints;
import com.cosmeticPlatform.CosmeticPlatform.model.PointTransaction;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.repository.LoyaltyRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.PointTransactionRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LoyaltyService {

    @Autowired
    private LoyaltyRepository loyaltyRepository;

    @Autowired
    private PointTransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public LoyaltyPoints getPoints(Integer userId) {
        return loyaltyRepository.findByUserId(userId)
                .orElseGet(() -> createInitialPoints(userId));
    }

    public List<PointTransaction> getHistory(Integer userId) {
        return transactionRepository.findByUserIdOrderByTransactionDateDesc(userId);
    }

    @Transactional
    public void earnPoints(Integer userId, int amount, String description) {
        if (amount <= 0)
            return;

        LoyaltyPoints lp = getPoints(userId);
        lp.setPointsBalance(lp.getPointsBalance() + amount);
        lp.setTotalPointsEarned(lp.getTotalPointsEarned() + amount);
        loyaltyRepository.save(lp);

        createTransaction(userId, amount, description);
    }

    @Transactional
    public boolean redeemPoints(Integer userId, int amount, String description) {
        if (amount <= 0)
            return false;

        LoyaltyPoints lp = getPoints(userId);
        if (lp.getPointsBalance() < amount) {
            return false;
        }

        lp.setPointsBalance(lp.getPointsBalance() - amount);
        loyaltyRepository.save(lp);

        createTransaction(userId, -amount, description);
        return true;
    }

    private LoyaltyPoints createInitialPoints(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        LoyaltyPoints lp = new LoyaltyPoints();
        lp.setUser(user);
        lp.setPointsBalance(0);
        lp.setTotalPointsEarned(0);
        return loyaltyRepository.save(lp);
    }

    private void createTransaction(Integer userId, int points, String description) {
        User user = userRepository.getReferenceById(userId);
        PointTransaction tx = new PointTransaction();
        tx.setUser(user);
        tx.setPoints(points);
        tx.setDescription(description);
        transactionRepository.save(tx);
    }
}

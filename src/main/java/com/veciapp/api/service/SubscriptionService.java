package com.veciapp.api.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veciapp.api.dto.SubscriptionPlanResponse;
import com.veciapp.api.dto.UpdateSubscriptionRequest;
import com.veciapp.api.dto.UserSubscriptionResponse;
import com.veciapp.api.entity.User;
import com.veciapp.api.model.SubscriptionPlan;
import com.veciapp.api.model.SubscriptionStatus;
import com.veciapp.api.repository.UserRepository;

@Service
public class SubscriptionService {

    private final UserRepository userRepository;

    public SubscriptionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<SubscriptionPlanResponse> listPlans() {
        return List.of(
                new SubscriptionPlanResponse(
                        SubscriptionPlan.BASIC,
                        "Basico",
                        BigDecimal.ZERO,
                        List.of(
                                "Reportes basicos",
                                "Cola de atencion estandar",
                                "Alertas de emergencia",
                                "Historial limitado a 7 dias")),
                new SubscriptionPlanResponse(
                        SubscriptionPlan.PREMIUM,
                        "Premium",
                        new BigDecimal("9.90"),
                        List.of(
                                "Reportes con prioridad alta",
                                "Atencion preferente por comisaria",
                                "Seguimiento en tiempo real",
                                "Historial completo",
                                "Notificaciones de estado",
                                "Soporte prioritario 24/7")),
                new SubscriptionPlanResponse(
                        SubscriptionPlan.FAMILY,
                        "Familiar",
                        new BigDecimal("19.90"),
                        List.of(
                                "Todo lo de Premium",
                                "Hasta 5 miembros del hogar",
                                "Mapa familiar",
                                "Ubicacion en tiempo real",
                                "Alertas familiares compartidas",
                                "Prioridad maxima")));
    }

    public UserSubscriptionResponse getCurrent(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return new UserSubscriptionResponse(
                user.getSubscriptionPlan(),
                user.getSubscriptionStatus(),
                user.getSubscriptionActivatedAt());
    }

    @Transactional
    public UserSubscriptionResponse update(Long userId, UpdateSubscriptionRequest request) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setSubscriptionPlan(request.plan());
        user.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        user.setSubscriptionActivatedAt(OffsetDateTime.now());
        User saved = userRepository.save(user);
        return new UserSubscriptionResponse(
                saved.getSubscriptionPlan(),
                saved.getSubscriptionStatus(),
                saved.getSubscriptionActivatedAt());
    }
}


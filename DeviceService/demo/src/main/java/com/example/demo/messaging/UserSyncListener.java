package com.example.demo.messaging;

import com.example.demo.dtos.UserSyncDTO;
import com.example.demo.entities.Users;
import com.example.demo.repositories.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


import com.example.demo.dtos.DeviceSyncDTO;

import com.example.demo.repositories.DeviceRepository;

import com.example.demo.repositories.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;



@Service
public class UserSyncListener {

    private final UserRepository userRepo;

    public UserSyncListener(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @RabbitListener(queues = "${rabbitmq.queue.user.sync}")
    public void onUserSync(UserSyncDTO event) {
        if (event.getOperation().equals("CREATE")) {
            userRepo.save(new Users(
                    event.getUserId()
            ));
        }

        if (event.getOperation().equals("DELETE")) {
            userRepo.deleteById(event.getUserId());
        }
    }
}

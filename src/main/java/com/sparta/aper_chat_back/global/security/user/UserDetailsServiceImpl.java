package com.sparta.aper_chat_back.global.security.user;

import com.aperlibrary.user.entity.User;
import com.sparta.aper_chat_back.global.security.exception.ServiceException;
import com.sparta.aper_chat_back.chat.enums.ErrorCode;
import com.sparta.aper_chat_back.global.security.user.respository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmailWithOutDeleteAccount(email)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        return new UserDetailsImpl(user);
    }
}
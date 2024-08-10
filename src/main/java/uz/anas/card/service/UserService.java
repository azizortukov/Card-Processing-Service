package uz.anas.card.service;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import uz.anas.card.model.dto.LoginDTO;
import uz.anas.card.model.dto.SignUpDTO;

@Service
public interface UserService {
    HttpEntity<?> checkLoginDetails( LoginDTO loginDto);

    HttpEntity<?> singUp( SignUpDTO signUpDTO);
}

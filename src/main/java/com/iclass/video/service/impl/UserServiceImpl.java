package com.iclass.video.service.impl;

import com.iclass.video.constants.RoleConstants;
import com.iclass.video.dto.request.auth.LoginDTO;
import com.iclass.video.dto.request.user.*;
import com.iclass.video.dto.response.user.UserAuthResponseDTO;
import com.iclass.video.dto.response.user.UserResponseDTO;
import com.iclass.video.entity.*;
import com.iclass.video.exception.BadRequestException;
import com.iclass.video.repository.*;
import com.iclass.video.exception.DuplicateEntityException;
import com.iclass.video.exception.ResourceNotFoundException;
import com.iclass.video.mapper.UserMapper;
import com.iclass.video.security.JwtService;
import com.iclass.video.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final BranchRepository branchRepository;
    private final UserCompanyRepository userCompanyRepository;
    private final UserBranchRepository userBranchRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();
        return userMapper.toResponseDTOList(users);
    }

    @Override
    public List<UserResponseDTO> findByRoleId(Integer roleId) {
        List<User> users = userRepository.findByRolId(roleId);
        return userMapper.toResponseDTOList(users);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO create(CreateUserDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateEntityException("Usuario", "username", dto.getUsername());
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEntityException("Usuario", "email", dto.getEmail());
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol", dto.getRoleId()));

        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        User user = userMapper.toEntity(dto, role, hashedPassword);
        User savedUser = userRepository.save(user);

        return userMapper.toResponseDTO(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO createCompanyAdmin(CreateCompanyAdminDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateEntityException("Usuario", "username", dto.getUsername());
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEntityException("Usuario", "email", dto.getEmail());
        }

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", dto.getCompanyId()));

        Role role = roleRepository.findById(2)
                .orElseThrow(() -> new ResourceNotFoundException("Rol ADMINISTRADOR_EMPRESA no encontrado"));

        String hashedPassword = passwordEncoder.encode("admin123!");

        User user = User.builder()
                .role(role)
                .username(dto.getUsername())
                .password(hashedPassword)
                .name(dto.getName())
                .paternalSurname(dto.getPaternalSurname())
                .maternalSurname(dto.getMaternalSurname())
                .documentNumber(dto.getDocumentNumber())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        UserCompany userCompany = UserCompany.builder()
                .user(savedUser)
                .company(company)
                .createdAt(LocalDateTime.now())
                .build();

        userCompanyRepository.save(userCompany);

        return userMapper.toResponseDTO(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO createBranchAdmin(CreateBranchAdminDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateEntityException("Usuario", "username", dto.getUsername());
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEntityException("Usuario", "email", dto.getEmail());
        }

        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal", dto.getBranchId()));

        Role role = roleRepository.findById(3)
                .orElseThrow(() -> new ResourceNotFoundException("Rol ADMINISTRADOR_SUCURSAL no encontrado"));

        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        User user = User.builder()
                .role(role)
                .username(dto.getUsername())
                .password(hashedPassword)
                .name(dto.getName())
                .paternalSurname(dto.getPaternalSurname())
                .maternalSurname(dto.getMaternalSurname())
                .documentNumber(dto.getDocumentNumber())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        UserBranch userBranch = UserBranch.builder()
                .user(savedUser)
                .branch(branch)
                .createdAt(LocalDateTime.now())
                .build();

        userBranchRepository.save(userBranch);

        return userMapper.toResponseDTO(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO update(Integer id, UpdateUserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new DuplicateEntityException("Usuario", "username", dto.getUsername());
            }
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new DuplicateEntityException("Usuario", "email", dto.getEmail());
            }
        }

        userMapper.updateEntity(user, dto);

        User updatedUser = userRepository.save(user);

        return userMapper.toResponseDTO(updatedUser);
    }

    @Override
    @Transactional
    public void reassignCompany(Integer userId, Integer newCompanyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", userId));

        if (user.getRole().getId() != RoleConstants.ID_ADMINISTRADOR_EMPRESA) {
            throw new BadRequestException("Solo se puede reasignar empresa a un Administrador de Empresa");
        }

        Company newCompany = companyRepository.findById(newCompanyId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", newCompanyId));

        userCompanyRepository.deleteByUser_Id(userId);

        UserCompany userCompany = UserCompany.builder()
                .user(user)
                .company(newCompany)
                .createdAt(LocalDateTime.now())
                .build();
        userCompanyRepository.save(userCompany);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", id);
        }

        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void activate(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        user.setIsActive(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deactivate(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetPassword(Integer id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public UserAuthResponseDTO login(LoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + dto.getUsername()));

        String token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        authentication.getAuthorities()
                )
        );

        return userMapper.userAuthResponseDTO(user, token);
    }
}

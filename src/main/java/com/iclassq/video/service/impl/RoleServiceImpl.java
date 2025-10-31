package com.iclassq.video.service.impl;

import com.iclassq.video.entity.Role;
import com.iclassq.video.repository.RoleRepository;
import com.iclassq.video.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}

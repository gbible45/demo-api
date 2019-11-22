package com.demo.api.portal.users.repository;

import com.demo.api.framework.repository.AbstractUserRepository;
import com.demo.api.portal.users.domain.PortalUser;
import com.demo.api.portal.users.domain.PortalUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends AbstractUserRepository<PortalUser, Long> {
    PortalUser findById(long id);
    List<PortalUser> findAllByDelUserYnOrderByIdDesc(String userYn);
    List<PortalUser> findAllByRoles(String role);
    Page<PortalUser> findByRoles(String role, Pageable pageable);
    Page<PortalUser> findByName(String name, Pageable pageable);
	List<PortalUser> findAllByEmailIn(Collection<String> emails);
}
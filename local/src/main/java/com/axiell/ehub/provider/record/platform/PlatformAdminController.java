/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.record.platform;

import com.axiell.ehub.provider.platform.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class PlatformAdminController implements IPlatformAdminController {

    @Autowired
    private IPlatformRepository platformRepository;

    @Override
    @Transactional(readOnly = true)
    public Platform getPlatform(final Long id) {
        return platformRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = false)
    public Platform save(final Platform platform) {
        return platformRepository.save(platform);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(final Platform platform) {
        final Platform initalizedPlatform = getPlatform(platform.getId());
        platformRepository.delete(initalizedPlatform);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Platform> findAll() {
        return platformRepository.findAllOrderedByName();
    }
}

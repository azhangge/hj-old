package com.hjedu.common.dao;

import java.util.List;

import com.hjedu.platform.entity.Logo;

public interface LogoDAO extends BaseDAO<Logo> {
	public abstract List<Logo> findAllLogoByBusinessId(String businessId);
	public abstract Logo findLogo(String id);
}

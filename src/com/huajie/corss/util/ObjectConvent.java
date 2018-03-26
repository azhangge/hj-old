package com.huajie.corss.util;

import java.util.Date;
import java.util.UUID;

import org.primefaces.json.JSONObject;

import com.hjedu.customer.entity.BbsUser;
import com.huajie.app.util.StringUtil;
import com.huajie.corss.model.SubUser;

public class ObjectConvent {
	public static SubUser BbsUser2SubUser(BbsUser bu){
    	SubUser subUser=new SubUser();
        if(StringUtil.isNotEmpty(bu.getExternalId())){
        	subUser.setId(bu.getExternalId().trim());
        }else{
        	subUser.setId(UUID.randomUUID().toString());
        }
        if(StringUtil.isNotEmpty(bu.getTel())){
        	subUser.setTel(bu.getTel().trim());
        }
        if(StringUtil.isNotEmpty(bu.getPassword())){
        	subUser.setPassword(bu.getPassword().trim());
        }
       if(StringUtil.isNotEmpty(bu.getUsername())){
    	   subUser.setUsername(bu.getUsername().trim());
       }
       if(StringUtil.isNotEmpty(bu.getEmail())){
    	   subUser.setEmail(bu.getEmail().trim());
       }
       if(StringUtil.isNotEmpty(bu.getRegIp())){
    	   subUser.setReg_ip(bu.getRegIp().trim());
       }
        if(StringUtil.isNotEmpty(bu.getName())){
	    	subUser.setName(bu.getName().trim());
        }
        if(StringUtil.isNotEmpty(bu.getPid())){
        	 subUser.setPid(bu.getPid().trim());
        }
        if(StringUtil.isNotEmpty(bu.getAddress())){
	    	subUser.setAddress(bu.getAddress().trim());
        }
        if(StringUtil.isNotEmpty(bu.getGender())){
			subUser.setGender(bu.getGender().trim());
        }
        subUser.setScore(bu.getScore());
        if(StringUtil.isNotEmpty(bu.getQq())){
        	subUser.setQq(bu.getQq().trim());
        }
        if(StringUtil.isNotEmpty(bu.getPosition())){
	    	subUser.setPosition(bu.getPosition().trim());
        }
        if(StringUtil.isNotEmpty(bu.getCompany())){
          	 subUser.setCompany(bu.getCompany().trim());
         }
        subUser.setReg_time(bu.getRegTime());
        subUser.setBirth_day(bu.getBirthDay());
        subUser.setLast_time(bu.getLastTime());
        subUser.setAvailable_time(bu.getAvailableTime());
        subUser.setExpire_time(bu.getExpireTime());
		return subUser;
    }
	
	public static BbsUser SubUser2BbsUser(JSONObject subUser,BbsUser us){
		if(!subUser.isNull("id")){
			if(StringUtil.isEmpty(us.getExternalId())){
				us.setExternalId((String)subUser.get("id"));
			}
    	}
		if(!subUser.isNull("tel")){
    		us.setTel((String)subUser.get("tel"));
    	}
    	if(!subUser.isNull("password")){
    		us.setPassword((String)subUser.get("password"));
    	}
    	if(!subUser.isNull("username")){
			us.setUsername((String) subUser.get("username"));
    	}
    	if(!subUser.isNull("email")){
    		us.setEmail((String)subUser.get("email"));
    	}
    	if(!subUser.isNull("reg_ip")){
    		us.setRegIp((String)subUser.get("reg_ip"));
    	}
    	if(!subUser.isNull("last_ip")){
    		us.setLastIp((String)subUser.get("last_ip"));
    	}
    	if(!subUser.isNull("name")){
			us.setName((String) subUser.get("name"));
    	}
    	if(!subUser.isNull("gender")){
			us.setGender((String) subUser.get("gender"));
    	}
    	if(!subUser.isNull("reg_time")){
    		us.setRegTime(new Date((long)subUser.get("reg_time")));
    	}
    	if(!subUser.isNull("birth_day")){
			us.setBirthDay(new Date((long)subUser.get("birth_day")));
    	}
		if(!subUser.isNull("score")){
			us.setScore((Integer)subUser.get("score"));  		
		}
		if(!subUser.isNull("address")){
			us.setAddress((String) subUser.get("address"));
		}
		if(!subUser.isNull("qq")){
			us.setQq((String)subUser.get("qq"));
		}
		if(!subUser.isNull("last_time")){
			us.setLastTime(new Date((long)subUser.get("last_time")));
		}
		if(!subUser.isNull("pid")){
			us.setPid((String)subUser.get("pid"));
		}
		if(!subUser.isNull("available_time")){
			us.setAvailableTime(new Date((long)subUser.get("available_time")));
    	}
		if(!subUser.isNull("expire_time")){
			us.setExpireTime(new Date((long)subUser.get("expire_time")));
		}
		if(!subUser.isNull("position")){
			us.setPosition((String) subUser.get("position"));
		}
		if(!subUser.isNull("company")){
			us.setCompany((String) subUser.get("company"));
		}
		return us;
	}

}

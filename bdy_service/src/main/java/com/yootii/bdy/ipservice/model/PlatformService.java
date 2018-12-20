package com.yootii.bdy.ipservice.model;

public class PlatformService {
    private Integer serviceId;

    private String serviceName;

    private String serviceDesc;
    
    private String serviceLogo;
    
    private Integer parent;
    
    private Object data;
    
    private String caseType;
    
    private Integer caseTypeId;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName == null ? null : serviceName.trim();
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc == null ? null : serviceDesc.trim();
    }
    
    public String getServiceLogo() {
        return serviceLogo;
    }

    public void setServiceLogo(String serviceLogo) {
        this.serviceLogo = serviceLogo == null ? null : serviceLogo.trim();
    }

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public Integer getCaseTypeId() {
		return caseTypeId;
	}

	public void setCaseTypeId(Integer caseTypeId) {
		this.caseTypeId = caseTypeId;
	}
}
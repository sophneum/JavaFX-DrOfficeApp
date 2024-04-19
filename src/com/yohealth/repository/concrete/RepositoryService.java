package com.yohealth.repository.concrete;

public class RepositoryService {
    private static UserRepositoryDBService userService;
    private static RoleRepositoryDBService roleService;
    private static MessageRepositoryDBService messageService;
    private static VisitRepositoryDBService visitService;
    private static PrescriptionRepositoryDBService prescriptionService;

    public static void initialize() {
        userService = new UserRepositoryDBService();
        roleService = new RoleRepositoryDBService();
        messageService = new MessageRepositoryDBService();
        visitService = new VisitRepositoryDBService();
        prescriptionService = new PrescriptionRepositoryDBService();
    }

    public static UserRepositoryDBService getUserService() {
        return userService;
    }

    public static RoleRepositoryDBService getRoleService() {
        return roleService;
    }

    public static MessageRepositoryDBService getMessageService() {
        return messageService;
    }

    public static VisitRepositoryDBService getVisitService() {
        return visitService;
    }

    public static PrescriptionRepositoryDBService getPrescriptionService() {
        return prescriptionService;
    }
}

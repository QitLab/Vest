package com.qit.plugin;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;

public class TestPlugin extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        NotificationGroup notificationGroup = new NotificationGroup("qwer", NotificationDisplayType.BALLOON, true);
        Notification notification = notificationGroup.createNotification("qwe", MessageType.INFO);
        Notifications.Bus.notify(notification);
        Messages.showMessageDialog(e.getData(PlatformDataKeys.PROJECT), "2212", "awsds", Messages.getInformationIcon());
//
    }
}

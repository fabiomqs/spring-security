import { Injectable } from '@angular/core';
import { NotifierService } from 'angular-notifier';
import { NotificationType } from '../../../enums/notification-type.enum';

@Injectable({providedIn: 'root'})
export class NotificationService {

    constructor(private notifier:NotifierService) { }

    private notify(type: NotificationType, message: string): void {
        this.notifier.notify(type, message);
    }

 //   sendNotificationError(message: string):void {
 //       if(message) 
 //           this.notify(NotificationType.ERROR, message);
 //       else
 //           this.notify(NotificationType.ERROR, 'An Unknow error has occurred!');
 //   }

 //   sendNotification(type: NotificationType, message: string):void {
 //       if(message) 
 //           this.notify(type, message);
 //       else
 //           this.notify(NotificationType.ERROR, 'An Unknow error has occurred!');
 //   }

    default(message: string):void {
        if(message) 
            this.notify(NotificationType.DEFAULT, message);
        else
            this.error(message);
    }

    success(message: string):void {
        if(message) 
            this.notify(NotificationType.SUCCESS, message);
        else
            this.error(message);
    }
    
    info(message: string):void {
        if(message) 
            this.notify(NotificationType.INFO, message);
        else
            this.error(message);
    }
    
    warning(message: string):void {
        if(message) 
            this.notify(NotificationType.WARNING, message);
        else
            this.error(message);
    }
    
    error(message: string):void {
        if(message) 
            this.notify(NotificationType.ERROR, message);
        else
            this.notify(NotificationType.ERROR, 'An Unknow error has occurred!');
    }

}
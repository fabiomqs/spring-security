import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, Subscription } from 'rxjs';
import { EnumMessages } from 'src/app/enums/enum-messages.enum';
import { NotificationType } from 'src/app/enums/notification-type.enum';
import { User } from 'src/app/model/user';
import { NotificationService } from 'src/app/service/notification.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit, OnDestroy {

    private titleSubject = new BehaviorSubject<string>('Users');
    titleAction$ = this.titleSubject.asObservable();
    users: User[];
    refreshing: boolean;
    private subscriptions: Subscription[] = [];

    constructor(private userService: UserService, 
                private notificationService: NotificationService) { }

    ngOnInit(): void {
        this.getUsers(true);
    }

    changeTitle(title: string): void {
        this.titleSubject.next(title);
    }

    getUsers(notification: boolean):void {
        this.refreshing = true;
        console.log('click')
        this.subscriptions.push(
            this.userService.getUsers().subscribe(
            (response: User[]) => {
                this.userService.addUsersToLocalCache(response);
                this.users = response;
                this.refreshing = false;
                if(notification) {
                    this.sendNotification(NotificationType.SUCCESS, 
                        `${response.length}${EnumMessages.USERS_LOADED_SUCCESS}`);
                }
            },
            (errorResponse: HttpErrorResponse) => {
                this.sendNotification(NotificationType.ERROR, errorResponse.error.message);
                this.refreshing = false;
            }
        ))
    }
    

    onResetPassword(f: any): void {

    }

    

    private sendNotification(type: NotificationType, message: string):void {
        if(message) 
            this.notificationService.notify(type, message);
        else
            this.notificationService.notify(NotificationType.ERROR, EnumMessages.UNKNOW_ERROR_MESSAGE);
    }

    ngOnDestroy(): void {
        this.subscriptions.forEach(sub => sub.unsubscribe());
    }

}

import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
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
    selectedUser: User;
    isAdmin: boolean = true;
    fileName: string;
    profileImage: File;

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
    
    onSelectUser(selectedUser: User):void {
        this.selectedUser = selectedUser;
        this.clickButton('openUserInfo');
    }
    
    onProfileImageChange(event: Event):void {
        const target = event.target as HTMLInputElement;
        const file: File = (target.files as FileList)[0];
        this.profileImage = file
        this.fileName = file.name;        
    }

    saveNewUser():void {
        this.clickButton('new-user-save');
    }

    onAddNewUser(userForm: NgForm):void {
        const formData = this.userService.createUserFormData(null, userForm.value, this.profileImage);
        this.subscriptions.push(
            this.userService.addUser(formData).subscribe(
                (response: User) => {
                    console.log(response);
                    this.clickButton('new-user-close');
                    this.getUsers(false);
                    this.fileName = null;
                    this.profileImage = null;
                    userForm.reset();
                    this.sendNotification(NotificationType.SUCCESS, 
                        `${response.firstName} ${response.lastName}${EnumMessages.USER_ADDED_SUCCESS}`);
                },
                (errorResponse: HttpErrorResponse) => {
                    this.sendNotification(NotificationType.ERROR, errorResponse.error.message);
                }
            )
        );
    }

    onResetPassword(f: any): void {

    }

    

    private sendNotification(type: NotificationType, message: string):void {
        if(message) 
            this.notificationService.notify(type, message);
        else
            this.notificationService.notify(NotificationType.ERROR, EnumMessages.UNKNOW_ERROR_MESSAGE);
    }

    private clickButton(buttonId: string): void {
        document.getElementById(buttonId).click();
    }

    ngOnDestroy(): void {
        this.subscriptions.forEach(sub => sub.unsubscribe());
    }

}

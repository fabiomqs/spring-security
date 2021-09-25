import { HttpErrorResponse, HttpEvent, HttpEventType } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { BehaviorSubject, Subscription } from 'rxjs';
import { EnumMessages } from 'src/app/enums/enum-messages.enum';
import { EnumRoutes } from 'src/app/enums/enum-routes.enum';
import { NotificationType } from 'src/app/enums/notification-type.enum';
import { CustomHttpResponse } from 'src/app/model/custom-http-response';
import { FileUploadStatus } from 'src/app/model/file-upload.status';
import { User } from 'src/app/model/user';
import { AuthenticationService } from 'src/app/service/authenticattion.service';
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
    selectedUser: User;
    isAdmin: boolean = true;
    fileName: string;
    profileImage: File;
    editUser = new User();
    user = new User();
    fileStatus = new FileUploadStatus();

    private subscriptions: Subscription[] = [];
    private currentUserName: string;

    constructor(private userService: UserService, 
                private router:  Router,
                private authenticationService:AuthenticationService,
                private notificationService: NotificationService) { }

    ngOnInit(): void {
        this.getUsers(true);
        //let init = true;
        this.user = this.authenticationService.getUserFromLocalCache();
        //this.subscriptions.push(
        //    this.titleSubject.subscribe(value => {
        //        if(value == 'Users' && !init) {
                    
        //        }
        //        if(init)
        //            init=false;
        //    })
        //);
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
                    this.profileImage = null;
                }
            )
        );
    }

    searchUsers(searchTerm: string):void {
        const results: User[] = [];
        this.userService
                .getUsersFromLocalCache()
                .forEach(user => {
                    if(user.firstName.toLocaleLowerCase().indexOf(searchTerm.toLocaleLowerCase()) != -1 || 
                        user.lastName.toLocaleLowerCase().indexOf(searchTerm.toLocaleLowerCase()) != -1 ||
                        user.username.toLocaleLowerCase().indexOf(searchTerm.toLocaleLowerCase()) != -1 ||
                        user.userId.toLocaleLowerCase().indexOf(searchTerm.toLocaleLowerCase()) != -1) {
                        
                            results.push(user);
                    }
                });
        this.users = results;
        if(!searchTerm) {
            this.users = this.userService.getUsersFromLocalCache();
        }
    }

    onEditUser(editUser: User): void {
        this.editUser = editUser;
        this.currentUserName = editUser.username;
        this.clickButton('openUserEdit');
    }

    onUpdateUser():void {
        const formData = this.userService.createUserFormData(this.currentUserName, this.editUser, this.profileImage);
        this.subscriptions.push(
            this.userService.updateUser(formData).subscribe(
                (response: User) => {
                    this.clickButton('closeEditUserModalButton');
                    this.getUsers(false);
                    this.fileName = null;
                    this.profileImage = null;
                    this.currentUserName = null;
                    this.editUser = new User();
                    this.sendNotification(NotificationType.SUCCESS, 
                        `${response.firstName} ${response.lastName}${EnumMessages.USER_UPDATED_SUCCESS}`);
                    
                },
                (errorResponse: HttpErrorResponse) => {
                    this.sendNotification(NotificationType.ERROR, errorResponse.error.message);
                    this.profileImage = null;
                }
            )
        );
    }

    onDeleteUser(username: string): void {
        this.subscriptions.push(
            this.userService.deleteUser(username).subscribe(
                (response: CustomHttpResponse) => {
                    this.sendNotification(NotificationType.SUCCESS, response.message);
                    this.getUsers(false);
                },
                (errorResponse: HttpErrorResponse) => {
                    this.sendNotification(NotificationType.ERROR, errorResponse.error.message);
                }
            )
        );
    }

    onResetPassword(emailForm: NgForm): void {
        this.refreshing = true;
        const emailAddress = emailForm.value['reset-password-email'];
        this.subscriptions.push(
            this.userService.resetpassword(emailAddress).subscribe(
                (response: CustomHttpResponse) => {
                    this.sendNotification(NotificationType.SUCCESS, response.message);
                    this.refreshing = false;
                },
                (errorResponse: HttpErrorResponse) => {
                    console.log(errorResponse)
                    let notificationType = NotificationType.WARNING;
                    if(errorResponse.status != 400)
                        notificationType = NotificationType.ERROR;
                    this.sendNotification(notificationType, errorResponse.error.message);
                    this.refreshing = false;
                },
                () => emailForm.reset()
            )
        );
    }

    onUpdateCurrentUser(user: User) :void {
        this.refreshing = true;
        this.currentUserName = this.authenticationService.getUserFromLocalCache().username;

        const formData = this.userService.createUserFormData(this.currentUserName, user, this.profileImage);
        this.subscriptions.push(
            this.userService.updateUser(formData).subscribe(
                (response: User) => {
                    this.authenticationService.addUserToLocalCache(response);
                    this.user = response;
                    this.getUsers(false);
                    this.fileName = null;
                    this.profileImage = null;
                    this.sendNotification(NotificationType.SUCCESS, 
                        `${response.firstName} ${response.lastName}${EnumMessages.USER_UPDATED_SUCCESS}`);
                    this.refreshing = false;
                    if(response.username != this.currentUserName)
                        this.onLogOut();
                },
                (errorResponse: HttpErrorResponse) => {
                    this.sendNotification(NotificationType.ERROR, errorResponse.error.message);
                    this.profileImage = null;
                    this.refreshing = false;
                }
            )
        );
    }

    updateProfileImage(): void {
        this.clickButton('profile-image-input');
    }

    onUpdateProfileImage():void {
        const formData = new FormData();
        formData.append('username', this.user.username);
        formData.append('profileImage',this.profileImage);
        
        this.subscriptions.push(
            this.userService.updateProfileImage(formData).subscribe(
                (event: HttpEvent<any>) => {
                    this.reportUploadProgress(event);
                },
                (errorResponse: HttpErrorResponse) => {
                    this.sendNotification(NotificationType.ERROR, errorResponse.error.message);
                    this.fileName = null;
                    this.profileImage = null;
                    this.fileStatus.status = 'done';
                }
            )
        );
    }

    private reportUploadProgress(event: HttpEvent<any>): void {
        switch(event.type) {
            case HttpEventType.UploadProgress:
                this.fileStatus.percentage = Math.round(100 * event.loaded / event.total);
                this.fileStatus.status = 'progress';
                break;
            case HttpEventType.Response:
                if(event.status === 200) {
                    this.user.profileImageUrl = `${event.body.profileImageUrl}?time=${new Date().getTime()}`
                    this.sendNotification(NotificationType.SUCCESS, 
                        `${event.body.firstName}${EnumMessages.IMAGE_UPLOADED_SUCCESS}`);
                    this.fileStatus.percentage = 100;
                    this.fileStatus.status = 'done';
                    this.fileName = null;
                    this.profileImage = null;
                    this.getUsers(false);
                    break;
                } else {
                    this.sendNotification(NotificationType.ERROR, 
                        `${EnumMessages.IMAGE_UPLOADED_FAILED}`);
                    this.fileName = null;
                    this.profileImage = null;
                    break;
                }
            default:
                break;
        }
    }

    onLogOut(): void {
        this.authenticationService.logOut();
        this.router.navigate([`/${EnumRoutes.LOGIN}`]);
        this.sendNotification(NotificationType.INFO, EnumMessages.USER_LOGGED_OUT_INFO);
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

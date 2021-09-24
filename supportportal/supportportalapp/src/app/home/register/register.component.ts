import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { EnumMessages } from 'src/app/enums/enum-messages.enum';
import { EnumRoutes } from 'src/app/enums/enum-routes.enum';
import { NotificationType } from 'src/app/enums/notification-type.enum';
import { User } from 'src/app/model/user';
import { AuthenticationService } from 'src/app/service/authenticattion.service';
import { NotificationService } from 'src/app/service/notification.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit, OnDestroy {

    showLoading: boolean;
    private subscriptions: Subscription[] = [];

    constructor(private router:Router,
        private authenticationService: AuthenticationService,
        private userService: UserService,
        private notificationService: NotificationService) { }
    
    ngOnInit(): void {
        if(this.authenticationService.isUserLoggedIn()) {
            this.router.navigateByUrl(`/${EnumRoutes.USERS}`);
        }
    }

    onRegister(user:User) {
        this.showLoading = true;
        console.log(user);
        this.subscriptions.push(
            this.userService.register(user).subscribe(
                (response: User) => {
                    this.showLoading = false;
                    this.sendNotification(NotificationType.SUCCESS, 
                    `${EnumMessages.REGISTER_SUCCESS_PREFIX}${response.username}.
                        ${EnumMessages.REGISTER_SUCCESS_SUFIX}`);
                    //this.router.navigateByUrl(`/${EnumRoutes.LOGIN}`);
                },
                error => {
                    console.log(error);
                    this.sendNotification(NotificationType.ERROR, error.error.message);
                    this.showLoading = false;
                }
            )
        );
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

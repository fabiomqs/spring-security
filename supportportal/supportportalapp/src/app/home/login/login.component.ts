import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { EnumMessages } from 'src/app/enums/enum-messages.enum';
import { EnumRoutes } from 'src/app/enums/enum-routes.enum';
import { HeaderType } from 'src/app/enums/header-type.enum';
import { NotificationType } from 'src/app/enums/notification-type.enum';
import { User } from 'src/app/model/user';
import { AuthenticationService } from 'src/app/service/authenticattion.service';
import { NotificationService } from 'src/app/service/notification.service';
import { SubSink } from 'subsink';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

    showLoading: boolean;
    private subs = new SubSink();
    //private subscriptions: Subscription[] = [];

    constructor(private router:Router,
                private authenticationService: AuthenticationService,
                private notificationService: NotificationService) { }
    

    ngOnInit(): void {
        if(this.authenticationService.isUserLoggedIn()) {
            this.router.navigateByUrl(`/${EnumRoutes.USERS}`);
        }
    }

    onLogin(user:User) {
        this.showLoading = true;
        //this.subscriptions.push(
        this.subs.add( 
            this.authenticationService.logIn(user).subscribe(
                (response: HttpResponse<User>) => {
                //response => {
                    response.headers.keys();
                    const token = response.headers.get(HeaderType.JWT_TOKEN);
                    this.authenticationService.saveToken(token);
                    this.authenticationService.addUserToLocalCache(response.body);
                    this.showLoading = false;
                    this.router.navigateByUrl(`/${EnumRoutes.USERS}`);
                    
                },
                (errorResponse: HttpErrorResponse) => {
                    this.sendErrorNotification(errorResponse.error.message);
                    this.showLoading = false;
                }
            )
        );
    }

    private sendErrorNotification(message: string):void {
        if(message) 
            this.notificationService.notify(NotificationType.ERROR, message);
        else
            this.notificationService.notify(NotificationType.ERROR, EnumMessages.UNKNOW_ERROR_MESSAGE);
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
        //this.subscriptions.forEach(sub => sub.unsubscribe());
    }

}

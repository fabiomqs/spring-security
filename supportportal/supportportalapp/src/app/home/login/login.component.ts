import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { EnumRoutes } from 'src/app/enums/enum-routes.enum';
import { NotificationType } from 'src/app/enums/notification-type.enum';
import { User } from 'src/app/model/user';
import { AuthenticationService } from 'src/app/service/authenticattion.service';
import { NotificationService } from 'src/app/service/notification.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

    public showLoading: boolean;
    private subscriptions: Subscription[] = [];

    constructor(private router:Router,
                private authenticationService: AuthenticationService,
                private notificationService: NotificationService) { }
    

    ngOnInit(): void {
        if(this.authenticationService.isUserLoggedIn()) {
            this.router.navigateByUrl(EnumRoutes.SLASH_USERS)
        }
    }

    public onLogin(user:User) {
        this.showLoading = true;
        console.log(user);
        this.subscriptions.push(
            this.authenticationService.logIn(user).subscribe(
                (response: HttpResponse<User>) => {
                //response => {
                    const token = response.headers.get('JWT-Token');
                    this.authenticationService.saveToken(token);
                    this.authenticationService.addUserToLocalCache(response.body);
                    this.showLoading = false;
                    this.router.navigateByUrl(EnumRoutes.SLASH_USERS);
                    
                },
                (errorResponse: HttpErrorResponse) => {
                    console.log(errorResponse);
                    this.sendErrorNotification(errorResponse.error.message);
                    this.showLoading = false;
                }
            )
        );
    }
    sendErrorNotification(m: string) {
        let message = 'An Unknow Error Occurred, please try again!'
        if(m) 
            message = m;
        this.notificationService.notify(NotificationType.ERROR, message);
    }

     ngOnDestroy(): void {
        this.subscriptions.forEach(sub => sub.unsubscribe());
    }

}

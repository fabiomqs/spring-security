import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/service/auth.service';
import { NotificationService } from 'src/app/core/service/notification.service';
import { NotificationType } from 'src/app/enums/notification-type.enum';
import { User } from 'src/app/model/user';
import { SubSink } from 'subsink';

@Component({
  templateUrl: './signin.component.html'
})
export class SigninComponent implements OnInit, OnDestroy {

    private subs = new SubSink();

    loginForm: FormGroup;

    constructor(
        private formBuilder: FormBuilder,
        private authService: AuthService,
        private notificationService: NotificationService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.loginForm = this.formBuilder.group({
            userName: ['', Validators.required],
            password: ['', Validators.required]
        });
    }

    login() {
        const username = this.loginForm.get('userName').value;
        const password = this.loginForm.get('password').value;
        const user = new User(username, password);
        this.subs.add(
            this.authService.authenticate(user)
                .subscribe(
                    (response: HttpResponse<User>) => {
                        console.log(response);
                        //this.router.navigateByUrl('user/' + username);
                        this.router.navigate(['user', username]);
                    },
                    (errorResponse: HttpErrorResponse) => {
                        console.log(errorResponse);
                        this.loginForm.reset();
                        this.notificationService
                            .sendNotificationError(errorResponse.error.message);
                    }
                )
        );
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

}

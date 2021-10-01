import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, ElementRef, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { SubSink } from 'subsink';

import { AuthService } from 'src/app/core/auth/service/auth.service';
import { NotificationService } from 'src/app/core/notification/service/notification.service';
import { User } from 'src/app/model/user';
import { PlatformDetectorService } from 'src/app/core/platform-detector/platform-detector.service';


@Component({
    templateUrl: './signin.component.html'
})
export class SigninComponent implements OnInit, OnDestroy {

    private subs = new SubSink();

    loginForm: FormGroup;
    @ViewChild('userNameInput') userNameInput: ElementRef<HTMLInputElement>;

    constructor(
        private formBuilder: FormBuilder,
        private authService: AuthService,
        private notificationService: NotificationService,
        private router: Router,
        private platformDetectorService: PlatformDetectorService
    ) { }

    ngOnInit(): void {
        this.loginForm = this.formBuilder.group({
            userName: ['', Validators.required],
            password: ['', Validators.required]
        });
        //this.platformDetectorService.isPlatformBrowser &&
        //                    this.userNameInput.nativeElement.focus();
    }

    login() {
        const username = this.loginForm.get('userName').value;
        const password = this.loginForm.get('password').value;
        const user = new User(username, password);
        this.subs.add(
            this.authService.authenticate(user)
                .subscribe(
                    (response: HttpResponse<User>) => {
                        //this.router.navigateByUrl('user/' + username);
                        this.router.navigate(['user', username]);
                    },
                    (errorResponse: HttpErrorResponse) => {
                        console.log(errorResponse);
                        this.loginForm.reset();
                        this.platformDetectorService.isPlatformBrowser &&
                            this.userNameInput.nativeElement.focus();
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

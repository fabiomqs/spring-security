import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NotificationService } from 'src/app/core/notification/service/notification.service';
import { NotificationType } from 'src/app/enums/notification-type.enum';
import { NewUser } from 'src/app/model/new-user';
import { User } from 'src/app/model/user';
import { lowerCaseValidator } from 'src/app/shared/validators/lower-case.validator';
import { SubSink } from 'subsink';
import { SignupService } from './service/signup.service';
import { UserNotTakenValidatorService } from './service/user-not-taken.validator.service';

@Component({
    selector: 'app-signup',
    templateUrl: './signup.component.html'
})
export class SignupComponent implements OnInit, OnDestroy {

    private subs = new SubSink();

    signupForm: FormGroup;

    constructor(
        private formBuilder: FormBuilder,
        private signupService: SignupService,
        private notificationService:NotificationService,
        private router: Router,
        private userNotTakenValidatorService:UserNotTakenValidatorService
    ) { }
    

    signup() {
        const newUser = this.signupForm.getRawValue() as NewUser;
        this.subs.add(
            this.signupService.signup(newUser)
                .subscribe(
                    (ret: User) => {
                        this.notificationService
                        .sendNotification(NotificationType.SUCCESS, 
                            `${ret.username} cadastrado com sucesso`);
                        this.router.navigate(['']);
                    },
                    err => console.log(err)
                )
        )
    }

    ngOnInit(): void {
        this.signupForm = this.formBuilder.group({
            firstName: ['', 
                [
                    Validators.required,
                    Validators.minLength(2),
                    Validators.maxLength(20)
                ]
            ],

            lastName: ['', 
                [
                    Validators.required,
                    Validators.minLength(2),
                    Validators.maxLength(30)
                ]
            ],

            email: ['', 
                [
                    Validators.required,
                    Validators.email
                ]
            ],
            username: ['', 
                [
                    Validators.required,
                    lowerCaseValidator,
                    Validators.minLength(2),
                    Validators.maxLength(30)
                ],
                this.userNotTakenValidatorService.checkUserNameTaken()
            ],
            password: ['', 
                [
                    Validators.required,
                    Validators.minLength(3),//TODO - min must be 8
                    Validators.maxLength(20)
                ]
            ]
        });
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

}

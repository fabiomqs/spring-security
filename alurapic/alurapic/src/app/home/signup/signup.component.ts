import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { lowerCaseValidator } from 'src/app/shared/validators/lower-case.validator';
import { UserNotTakenValidatorService } from './service/user-not-taken.validator.service';

@Component({
    selector: 'app-signup',
    templateUrl: './signup.component.html'
})
export class SignupComponent implements OnInit {

    signupForm: FormGroup;

    constructor(
        private formBuilder: FormBuilder,
        private userNotTakenValidatorService:UserNotTakenValidatorService
    ) { }

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

}

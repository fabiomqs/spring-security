import { Injectable, OnDestroy, OnInit } from "@angular/core";
import { AbstractControl } from "@angular/forms";
import { debounceTime, map, switchMap, first } from "rxjs/operators";


import { SignupService } from "./signup.service";

@Injectable()
export class UserNotTakenValidatorService {

    constructor(private signupService: SignupService) {

    }

    checkUserNameTaken() {
        return (control: AbstractControl) => {
            return control
                .valueChanges
                .pipe(debounceTime(300))
                .pipe(switchMap(username => 
                    this.signupService.checkUsernameTaken(username)
                ))
                .pipe(map(isTaken => isTaken ? { userNameTaken: true } : null))
            .pipe(first());
        }
    }
}
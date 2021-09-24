import { Component, OnDestroy, OnInit } from '@angular/core';
import { User } from 'src/app/model/user';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit, OnDestroy {

    showLoading: boolean;

    constructor() { }
    

    onRegister(user:User) {
    }
    

    ngOnInit(): void {
    }

    ngOnDestroy(): void {
        
    }

}

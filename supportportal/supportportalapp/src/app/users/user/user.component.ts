import { Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

    private titleSubject = new BehaviorSubject<string>('Users');
    titleAction$ = this.titleSubject.asObservable();

    constructor() { }

    changeTitle(title: string): void {
        this.titleSubject.next(title);
    }

    onResetPassword(f: any): void {
        
    }

    ngOnInit(): void {
    }

}

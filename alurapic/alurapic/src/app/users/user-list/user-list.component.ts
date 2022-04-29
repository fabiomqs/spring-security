import { Component, OnDestroy, OnInit } from '@angular/core';
import { SubSink } from 'subsink';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html'
})
export class UserListComponent implements OnInit, OnDestroy {

    private subs = new SubSink();

    constructor(private userService: UserService) { }

    ngOnInit(): void {
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

}

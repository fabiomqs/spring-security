import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserFormModule } from './user-form/user-form.module';
import { UserListModule } from './user-list/user-list.module';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    UserFormModule,
    UserListModule
  ]
})
export class UsersModule { }

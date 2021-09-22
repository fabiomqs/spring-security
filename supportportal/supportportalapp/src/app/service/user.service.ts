import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpEvent, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs';

import {environment} from '../../environments/environment';

import { User } from '../model/user';


@Injectable({providedIn: 'root'})
export class UserService {

    private host = environment.apiUrl;
    private apiPrefix = environment.prefix;
    private userPath = environment.user;

    constructor(private httpClient:HttpClient) { }

    public getUsers():Observable<User[] | HttpErrorResponse> {
        return this.httpClient.get<User[]>(
            `${this.host}${this.apiPrefix}${this.userPath}/list`);
    }

    public addUser(formData:FormData):Observable<User | HttpErrorResponse> {
        return this.httpClient.post<User>(
            `${this.host}${this.apiPrefix}${this.userPath}/add`, 
            formData);
    }

    public updateUser(formData:FormData):Observable<User | HttpErrorResponse> {
        return this.httpClient.post<User>(
            `${this.host}${this.apiPrefix}${this.userPath}/update`, 
            formData);
    }

    public resetpassword(email:string):Observable<any | HttpErrorResponse> {
        return this.httpClient.get(
            `${this.host}${this.apiPrefix}${this.userPath}/reset-password/${email}`);
    }

    public updateProfileImage(formData:FormData):Observable<HttpEvent<User> | HttpErrorResponse> {
        return this.httpClient.post<User>(
            `${this.host}${this.apiPrefix}${this.userPath}/update-profile-image`, 
            formData, {reportProgress: true, observe: 'events'});
    }

    public deleteUser(userId:number):Observable<any | HttpErrorResponse> {
        return this.httpClient.delete(
            `${this.host}${this.apiPrefix}${this.userPath}/delete/${userId}`);
    }

    public addUsersToLocalCache(users:User[]):void {
        localStorage.setItem('users', JSON.stringify(users));
    }

    public getUsersFromLocalCache():User[] {
        if(localStorage.getItem('users')) {
            return JSON.parse(localStorage.getItem('users'));
        }
        return null;
    }

    

    public register(user:User):Observable<User | HttpErrorResponse> {
        return this.httpClient.post<User>(
            `${this.host}${this.apiPrefix}${this.userPath}/register`, 
            user);
    }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpEvent, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs';

import {environment} from '../../environments/environment';

import { User } from '../model/user';
import { CustomHttpResponse } from '../model/custom-http-response';


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

    public resetpassword(email:string):Observable<CustomHttpResponse | HttpErrorResponse> {
        return this.httpClient.get<CustomHttpResponse>(
            `${this.host}${this.apiPrefix}${this.userPath}/reset-password/${email}`);
    }

    public updateProfileImage(formData:FormData):Observable<HttpEvent<User> | HttpErrorResponse> {
        return this.httpClient.post<User>(
            `${this.host}${this.apiPrefix}${this.userPath}/update-profile-image`, 
            formData, {reportProgress: true, observe: 'events'});
    }

    public deleteUser(userId:number):Observable<CustomHttpResponse | HttpErrorResponse> {
        return this.httpClient.delete<CustomHttpResponse>(
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

    public createUserFormData(loggedInUsername: string, user: User, profileImage: File):FormData {
        const formData = new FormData();
        formData.append('currentUsername', loggedInUsername);

        formData.append('firstName', user.firstName);
        formData.append('lastName', user.lastName);
        formData.append('username', user.username);
        formData.append('email', user.email);
        formData.append('role', user.role);
        formData.append('isNonLocked', JSON.stringify(user.notLocked));
        formData.append('isActive', JSON.stringify(user.active));
        
        formData.append('profileImage', profileImage);
        return formData;
    }

    

    public register(user:User):Observable<User | HttpErrorResponse> {
        return this.httpClient.post<User>(
            `${this.host}${this.apiPrefix}${this.userPath}/register`, 
            user);
    }
}

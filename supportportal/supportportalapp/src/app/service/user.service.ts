import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent } from '@angular/common/http';

import { Observable } from 'rxjs';

import {environment} from '../../environments/environment';

import { User } from '../model/user';
import { CustomHttpResponse } from '../model/custom-http-response';
import { EnumKeyType } from '../enums/enum-key-type.enum';
import { EnumRoutes } from '../enums/enum-routes.enum';


@Injectable({providedIn: 'root'})
export class UserService {

    public host = environment.apiUrl;
    public apiPrefix = environment.prefix;
    public userPath = environment.user;

    constructor(private httpClient:HttpClient) { }

    public getUsers():Observable<User[]> {
        return this.httpClient.get<User[]>(
            `${this.host}${this.apiPrefix}${this.userPath}/${EnumRoutes.LIST}`);
    }

    public addUser(formData:FormData):Observable<User> {
        return this.httpClient.post<User>(
            `${this.host}${this.apiPrefix}${this.userPath}/${EnumRoutes.ADD}`, 
            formData);
    }

    public updateUser(formData:FormData):Observable<User> {
        return this.httpClient.post<User>(
            `${this.host}${this.apiPrefix}${this.userPath}/${EnumRoutes.UPDATE}`, 
            formData);
    }

    public resetpassword(email:string):Observable<CustomHttpResponse> {
        return this.httpClient.get<CustomHttpResponse>(
            `${this.host}${this.apiPrefix}${this.userPath}/${EnumRoutes.RESET_PASSWORD}/${email}`);
    }

    public updateProfileImage(formData:FormData):Observable<HttpEvent<User>> {
        return this.httpClient.post<User>(
            `${this.host}${this.apiPrefix}${this.userPath}/${EnumRoutes.UPDATE_PROFILE_IMAGE}`, 
            formData, {reportProgress: true, observe: 'events'});
    }

    public deleteUser(userId:number):Observable<CustomHttpResponse> {
        return this.httpClient.delete<CustomHttpResponse>(
            `${this.host}${this.apiPrefix}${this.userPath}/${EnumRoutes.DELETE}/${userId}`);
    }

    public addUsersToLocalCache(users:User[]):void {
        localStorage.setItem(EnumKeyType.USERS, JSON.stringify(users));
    }

    public getUsersFromLocalCache():User[] {
        if(localStorage.getItem(EnumKeyType.USERS)) {
            return JSON.parse(localStorage.getItem(EnumKeyType.USERS));
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

    public register(user:User):Observable<User> {
        return this.httpClient.post<User>(
            `${this.host}${this.apiPrefix}${this.userPath}/${EnumRoutes.REGISTER}`, 
            user, {withCredentials: true});
    }
    
}

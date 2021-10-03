import { HttpClient, HttpEvent, HttpParams } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { APIURL } from 'src/app/core/tokens';
import { UserService } from 'src/app/core/user/user.service';
import { Photo } from '../../model/photo';

@Injectable({
    providedIn: 'root'
})
export class PhotoService {

    constructor(
        private http:HttpClient,
        private userService: UserService,
        @Inject(APIURL) private apiUrl: string,
        
    ) { }

    listFromUser(username: string):Observable<Photo[]> {
        return this.http
                .get<Photo[]>(`${this.apiUrl}/api/v1/photos/user/${username}`)
    }

    listFromUserPaginated(username: string, page: number, size:number):Observable<Photo[]> {
        const params = new HttpParams()
                .append('page', page.toString())
                .append('size', size.toString());
        return this.http
                .get<Photo[]>(`${this.apiUrl}/api/v1/photos/user/${username}`, { params })
    }

    upload(description: string, allowComments: boolean, photo: string):Observable<HttpEvent<Photo>> {
        const formData = new FormData();
        formData.append('username', this.userService.getUsername())
        formData.append('description', description)
        formData.append('allowComments', allowComments ? 'true' : 'false');
        formData.append('photo', photo);
        return this.http
                .post<Photo>(`${this.apiUrl}/api/v1/photos/photo/upload/base64`, 
                formData, {reportProgress: true, observe: 'events'});
    }

    findById(photoId: number):Observable<Photo> {
        return this.http
                .get<Photo>(`${this.apiUrl}/api/v1/photos/user/photo/${photoId}`)
    }

    getComments(photoId: number):Observable<Comment[]> {
        return this.http
                .get<Comment[]>(`${this.apiUrl}/api/v1/photos/comment/all/${photoId}`)
    }

    getCommentsPaginated(photoId: number, page: number, size:number):Observable<Comment[]> {
        const params = new HttpParams()
                .append('page', page.toString())
                .append('size', size.toString());
        return this.http
                .get<Comment[]>(`${this.apiUrl}/api/v1/photos/comment/${photoId}`, { params })
    }

    addComment(photoId: number, comment: string) {
        const formData = new FormData();
        formData.append('username', this.userService.getUsername());
        formData.append('comment', comment);
        return this.http
                .post(`${this.apiUrl}/api/v1/photos/comment/${photoId}`, 
                formData);
    }

}

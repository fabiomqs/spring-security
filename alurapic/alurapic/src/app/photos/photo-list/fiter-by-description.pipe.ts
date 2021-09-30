import { Pipe, PipeTransform } from '@angular/core';
import { Photo } from '../photo/photo';

@Pipe({
    name: 'fiterByDescription'
})
export class FiterByDescriptionPipe implements PipeTransform {

    transform(photos: Photo[], descriptionQuery: string): Photo[] {
        descriptionQuery = descriptionQuery
                .trim()
                .toLowerCase();
        if(descriptionQuery && descriptionQuery != '') {
            return photos.filter(photo => 
                photo.description.toLowerCase().includes(descriptionQuery)
            );
        } else {
            return photos;
        }

    }

}

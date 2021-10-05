import { FormGroup, ValidatorFn } from "@angular/forms";

export const usernamePassword: ValidatorFn = (formGroup:FormGroup) => {
    const username = formGroup.get('username').value;
    const password = formGroup.get('password').value;
    if(username.trim() + password.trim()) {
        return username != password
            ? null
            : { usernamePassword: true}
    } else {
        return null
    }
}
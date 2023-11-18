import { Router } from "@angular/router";
import { AuthGuard } from "./auth.guard";
import { SessionService } from "../services/session.service";


describe('AuthGard', ()=>{
    let authGuard : AuthGuard;
    let router : Router;
    let sessionService : SessionService;

    beforeEach(()=>{
        router = {
            navigate : jest.fn(),
        } as any;
        sessionService = {
            isLogged : true,
        } as any;
        authGuard = new AuthGuard(router, sessionService);
    });

    it('Redirection vers login ', ()=>{
        sessionService.isLogged = false;
        let canActivate = authGuard.canActivate();
        expect(canActivate).toBeFalsy();
        expect(router.navigate).toHaveBeenCalledWith(['login']);
    });

    it('canActivate return true si user est connected', () => {
        let canActivate = authGuard.canActivate();
        expect(canActivate).toBeTruthy();
    });
});
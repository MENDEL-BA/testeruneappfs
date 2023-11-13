
describe('Redirection vers la page not found', ()=>{
    it('Not found page si la page existe pas', ()=>{
        cy.request({url:'/totoPage',failOnStatusCode:false})
            .its('status')
            .should('equal', 404);
        cy.visit({url:'/totoPage',failOnStatusCode:false});
        cy.get('h1').contains('Page not found !');

    });
});
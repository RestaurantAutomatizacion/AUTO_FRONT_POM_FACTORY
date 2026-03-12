@flujo-e2e
Feature: Flujo E2E completo del Sistema de Tickets

  Como usuario del Sistema de Tickets,
  quiero poder iniciar sesión, crear tickets y consultarlos
  para gestionar mis solicitudes de soporte de extremo a extremo.

  @smoke
  Scenario Outline: Flujo E2E completo: login, creación y consulta de ticket
    Given el usuario "<username>" existe en el sistema con email "<email>" y contraseña "<password>"
    When el usuario inicia sesión
    And crea un ticket con título "<titulo>" y descripción "<descripcion>"
    Then el ticket "<titulo>" aparece en su lista de solicitudes
    And puede consultar el detalle del ticket "<titulo>"

    Examples:
      | username    | email                      | password      | titulo         | descripcion                                  |
      | e2eflow2027 | e2eflow2027@test.sofka.com | TestPass@2026 | Ticket nuevo 2028 | Ticket creado #55 de danii en flujo automatizado          |

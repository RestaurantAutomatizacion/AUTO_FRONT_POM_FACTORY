@registro
Feature: Registro de usuario en el Sistema de Tickets

  Como usuario anónimo del Sistema de Tickets,
  quiero poder crear mi cuenta en la plataforma
  para gestionar mis solicitudes de soporte.

  @happy-path
  Scenario Outline: Registro exitoso de un nuevo usuario
    When el usuario se registra con usuario "<username>", email "<email>" y contraseña "<password>"
    Then el usuario queda autenticado en el sistema

    Examples:
      | username | email                    | password        |
      | Daniel398 | userDaniel@test.sofka.com | Daniel2Tess@2027 |

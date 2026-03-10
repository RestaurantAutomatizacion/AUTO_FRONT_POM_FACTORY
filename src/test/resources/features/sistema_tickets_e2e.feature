@e2e @flujo-completo
Feature: Flujo completo E2E del Sistema de Tickets

  Como usuario del Sistema de Tickets,
  quiero poder registrarme, iniciar sesión, crear tickets y visualizarlos
  para gestionar mis solicitudes de soporte de extremo a extremo.

  Background:
    Given el usuario navega a la aplicación

  # ===========================================================================
  # HU-1: Registro de nuevo usuario
  # ===========================================================================

  @registro @happy-path
  Scenario: Registro exitoso de un nuevo usuario
    When el usuario navega a la página de registro
    And completa el formulario de registro con:
      | username  | email                    | password      |
      | ale398    | userNuevo12@test.sofka.com  | nuevo22Tess@2027 |
    Then debería ser redirigido a la lista de tickets
    And la barra de navegación debería estar visible

  @registro @validacion
  Scenario: Registro con contraseñas que no coinciden
    When el usuario navega a la página de registro
    And introduce el nombre de usuario "testuser_mismatch"
    And introduce el email "mismatch@test.sofka.com"
    And introduce la contraseña "TestPass@2026"
    And introduce la confirmación de contraseña "DiferentPass@2026"
    And hace click en "Crear cuenta"
    Then debería ver el error "Las contraseñas no coinciden"

  # ===========================================================================
  # HU-2: Inicio de sesión
  # ===========================================================================

  @login @happy-path
  Scenario: Login exitoso con usuario registrado
    Given un usuario con email "admin@sofkau.com" y contraseña "Admin@SofkaU_2026!" existe en el sistema
    When el usuario introduce el email "admin@sofkau.com"
    And el usuario introduce la contraseña "Admin@SofkaU_2026!"
    And el usuario hace click en "Iniciar sesión"
    Then debería ser redirigido a la lista de tickets

  @login @validacion
  Scenario: Login con credenciales incorrectas
    When el usuario introduce el email "incorrecto@test.com"
    And el usuario introduce la contraseña "ClaveIncorrecta123!"
    And el usuario hace click en "Iniciar sesión"
    Then debería ver un mensaje de error de autenticación

  # ===========================================================================
  # HU-5: Flujo E2E completo ( login → crear ticket → verificar)
  # ===========================================================================

  @flujo-e2e @smoke
  Scenario: Flujo E2E completo desde registro hasta verificación de ticket
    Given el usuario navega a la página de login
    When el usuario ingresa el email "e2eflow2026@test.sofka.com"
    And ingresa la contraseña "TestPass@2026"
    And hace click en el botón de login
    Then debería ser redirigido a la lista de tickets
    When el usuario navega a "Crear Ticket"
    And completa el formulario de ticket con título "Ticket de usuario otra vez" y descripción "Este ticket fue creado durante el flujo E2E completo del usuario registrado para valuiadar el nuevo ticket"
    And envía el formulario del ticket
    Then debería ser redirigido a la lista de tickets
    And el ticket "Ticket de usuario otra vez" debería aparecer en la lista
    When el usuario hace click en el ticket "Ticket de usuario otra vez"
    Then debería ver el detalle del ticket
    And el título del detalle debería contener "Ticket de usuario otra vez"

  # ===========================================================================
  # HU-6: Gestión de asignaciones (administrador)
  # ===========================================================================

  @asignaciones @admin
  Scenario: Administrador accede a la vista de asignaciones
    Given el usuario está autenticado con email "admin@sofkau.com" y contraseña "Admin@SofkaU_2026!"
    When el administrador navega a "Asignaciones"
    Then la página de asignaciones debería estar cargada

  # ===========================================================================
  # HU-7: Cierre de sesión
  # ===========================================================================

  @logout
  Scenario: Cierre de sesión exitoso
    Given el usuario está autenticado con email "admin@sofkau.com" y contraseña "Admin@SofkaU_2026!"
    When el usuario cierra sesión
    Then debería ser redirigido a la página de login

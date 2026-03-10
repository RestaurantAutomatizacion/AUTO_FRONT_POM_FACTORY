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
      | username | email                    | password      |
      | e2euser1 | e2euser1@test.sofka.com  | TestPass@2026 |
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
  # HU-3: Creación de ticket (usuario autenticado)
  # ===========================================================================

  @creacion-ticket @happy-path
  Scenario: Creación de ticket exitosa por usuario autenticado
    Given el usuario está autenticado con email "admin@sofkau.com" y contraseña "Admin@SofkaU_2026!"
    When el usuario navega a "Crear Ticket"
    And completa el formulario de ticket con título "Ticket E2E Automatizado" y descripción "Descripción generada por prueba automatizada E2E"
    And envía el formulario del ticket
    Then debería ser redirigido a la lista de tickets
    And el ticket "Ticket E2E Automatizado" debería aparecer en la lista

  @creacion-ticket @formulario
  Scenario: Visualización del formulario de creación de ticket
    Given el usuario está autenticado con email "admin@sofkau.com" y contraseña "Admin@SofkaU_2026!"
    When el usuario navega a "Crear Ticket"
    Then la página de creación de ticket debería estar cargada
    And el formulario debería tener los campos de título y descripción

  # ===========================================================================
  # HU-4: Visualización y gestión de tickets
  # ===========================================================================

  @lista-tickets @happy-path
  Scenario: Visualización de la lista de tickets
    Given el usuario está autenticado con email "admin@sofkau.com" y contraseña "Admin@SofkaU_2026!"
    When el usuario navega a la lista de tickets
    Then la página de tickets debería estar cargada
    And debería ver la lista de tickets del sistema

  @detalle-ticket @happy-path
  Scenario: Acceso al detalle de un ticket existente
    Given el usuario está autenticado con email "admin@sofkau.com" y contraseña "Admin@SofkaU_2026!"
    And existe al menos un ticket en el sistema
    When el usuario hace click en el primer ticket de la lista
    Then debería ver el detalle del ticket
    And debería ver el estado del ticket
    And debería ver la sección de respuestas

  # ===========================================================================
  # HU-5: Flujo E2E completo (registro → login → crear ticket → verificar)
  # ===========================================================================

  @flujo-e2e @smoke
  Scenario: Flujo E2E completo desde registro hasta verificación de ticket
    When el usuario navega a la página de registro
    And completa el formulario de registro con:
      | username    | email                      | password      |
      | e2eflow2026 | e2eflow2026@test.sofka.com | TestPass@2026 |
    Then debería ser redirigido a la lista de tickets
    When el usuario navega a "Crear Ticket"
    And completa el formulario de ticket con título "Ticket del Flujo E2E" y descripción "Este ticket fue creado durante el flujo E2E completo"
    And envía el formulario del ticket
    Then debería ser redirigido a la lista de tickets
    And el ticket "Ticket del Flujo E2E" debería aparecer en la lista
    When el usuario hace click en el ticket "Ticket del Flujo E2E"
    Then debería ver el detalle del ticket
    And el título del detalle debería contener "Ticket del Flujo E2E"

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

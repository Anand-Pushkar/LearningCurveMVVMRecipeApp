package dependencies


object AndroidX {

  const val core_ktx = "androidx.core:core-ktx:${Version.core_ktx}"
  const val app_compat = "androidx.appcompat:appcompat:${Version.app_compat}"
  const val constraint_layout = "androidx.constraintlayout:constraintlayout:${Version.constraint_layout}"
  const val ui_tooling = "androidx.ui:ui-tooling:${Version.androidx_ui}"

  // we will remove these 2 dependencies later when building the compose-only nav system
  const val nav_fragment_ktx = "androidx.navigation:navigation-fragment-ktx:${Version.nav_component}"
  const val nav_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Version.nav_component}"

  const val compose_ui = "androidx.compose.ui:ui:${Version.compose}"
  const val compose_foundation = "androidx.compose.foundation:foundation:${Version.compose}"
  const val runtime_livedata = "androidx.compose.runtime:runtime-livedata:${Version.compose}"
  const val runtime_rxjava2 = "androidx.compose.runtime:runtime-rxjava2:${Version.compose}"
  const val compose_material = "androidx.compose.material:material:${Version.compose}"
  const val compose_icons_core = "androidx.compose.material:material-icons-core:${Version.compose}"
  const val compose_icons_extended = "androidx.compose.material:material-icons-extended:${Version.compose}"

  const val navigation_compose = "androidx.navigation:navigation-compose:${Version.nav_compose}"
  const val navigation_hilt = "androidx.hilt:hilt-navigation:${Version.hilt_navigation}"

  const val room_runtime = "androidx.room:room-runtime:${Version.room}"
  const val room_ktx = "androidx.room:room-ktx:${Version.room}"

  const val datastore = "androidx.datastore:datastore-preferences:${Version.datastore}"

  const val hilt_lifecycle_viewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:${Version.hilt_lifecycle_viewmodel}"

  const val hilt_navigation_fragment = "androidx.hilt:hilt-navigation-fragment:${Version.hilt_lifecycle}"
  const val hilt_work = "androidx.hilt:hilt-work:${Version.hilt_lifecycle}"

}



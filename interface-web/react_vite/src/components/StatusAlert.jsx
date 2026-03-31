function StatusAlert({ message, isError = false }) {
  if (!message) return null

  return (
    <p
      className={`mt-3 mb-0 ${isError ? 'text-danger' : 'text-success'}`}
      role="status"
      aria-live="polite"
    >
      {message}
    </p>
  )
}

export default StatusAlert

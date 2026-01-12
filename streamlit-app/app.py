import streamlit as st
import requests
import uuid

# Configuration
API_BASE_URL = "http://localhost:8081/api/agent"
CHAT_ENDPOINT = f"{API_BASE_URL}/chat"

# Initialize session state
if 'session_id' not in st.session_state:
    st.session_state.session_id = str(uuid.uuid4())

if 'chat_history' not in st.session_state:
    st.session_state.chat_history = []

# Custom CSS for better styling
st.markdown("""
<style>
.chat-message {
    padding: 10px;
    margin: 5px 0;
    border-radius: 10px;
}
.user-message {
    background-color: #e3f2fd;
    text-align: right;
}
.ai-message {
    background-color: #f5f5f5;
}
</style>
""", unsafe_allow_html=True)

st.title("🤖 SmartRent AI Assistant")
st.markdown("*Chat with the AI assistant that uses MCP tools for reservation and location services.*")

# Sidebar for session info
with st.sidebar:
    st.header("Session Info")
    st.write(f"Session ID: {st.session_state.session_id[:8]}...")
    if st.button("New Session"):
        st.session_state.session_id = str(uuid.uuid4())
        st.session_state.chat_history = []
        st.success("New session started!")

# Display chat history
st.subheader("💬 Chat History")
chat_container = st.container(height=400, border=True)

with chat_container:
    for message in st.session_state.chat_history:
        if message['role'] == 'user':
            st.markdown(f'<div class="chat-message user-message"><strong>You:</strong> {message["content"]}</div>', unsafe_allow_html=True)
        else:
            st.markdown(f'<div class="chat-message ai-message"><strong>AI:</strong> {message["content"]}</div>', unsafe_allow_html=True)
            if 'tool_executions' in message and message['tool_executions']:
                with st.expander("🔧 Tool Execution Details"):
                    for tool in message['tool_executions']:
                        st.code(tool, language='text')

# Input for new message
st.subheader("💬 Send a Message")
col1, col2 = st.columns([4, 1])
with col1:
    user_input = st.text_input("Type your message here...", key="user_input", label_visibility="collapsed")
with col2:
    send_button = st.button("Send", key="send_button", use_container_width=True)

if send_button and user_input.strip():
    # Add user message to history
    st.session_state.chat_history.append({
        'role': 'user',
        'content': user_input,
        'timestamp': st.session_state.get('timestamp', 0)
    })

    # Send request to API
    payload = {
        "message": user_input,
        "sessionId": st.session_state.session_id
    }

    with st.spinner("AI is thinking..."):
        try:
            response = requests.post(CHAT_ENDPOINT, json=payload, timeout=30)
            if response.status_code == 200:
                data = response.json()
                ai_response = data.get('response', 'No response')

                # Parse tool executions from response
                tool_executions = []
                clean_response = ai_response
                if '[' in ai_response and ']' in ai_response:
                    parts = ai_response.split('\n\n')
                    clean_parts = []
                    for part in parts:
                        if part.startswith('[Service'):
                            tool_executions.append(part)
                        else:
                            clean_parts.append(part)
                    clean_response = '\n\n'.join(clean_parts)

                # Add AI response to history
                st.session_state.chat_history.append({
                    'role': 'assistant',
                    'content': clean_response,
                    'timestamp': data.get('timestamp', 0),
                    'tool_executions': tool_executions
                })

                st.rerun()  # Refresh to show new messages

            else:
                st.error(f"Error: {response.status_code} - {response.text}")
        except requests.exceptions.RequestException as e:
            st.error(f"Request failed: {str(e)}")

    # Clear input
    st.session_state.user_input = ""

# Footer
st.markdown("---")
st.markdown("*Powered by SmartRent AI with MCP tools*")